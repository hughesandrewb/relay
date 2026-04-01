# Relay Real-Time Gateway

This document outlines the specifics of the real-time gateway of Relay.

## Background & Motivation

Users expect to retrieve notifications of realtime events while using any platform but especially a messaging platform. Real-time events include the obvious messages sent in rooms they are part of, friend's presence status updates, typing indicators, but also includes events for workspace updates (workspace name), friends profile updates (profile image, display name), new workspace roles, and countless other events that need to be sent to keep the client up to date without having to do a full refresh. The main challenge with a realtime gateway is scalability. The gateway will have different scaling properties from the rest of the application. The following outlines the solutions to this challenge and its integration with the applicaiton as a whole.

## Design

### Client-Server Connection

The primary technology used by the real-time gateway to connection clients with the server is WebSocket. WebSocket is built on top of TCP and allows for efficient bi-directional communication between a client and server. WebSocket was chosen as the primary technology because its the dominant real-time communication protocol used today with implementations in every major web and native environment. WebTransport is another technology that was researched but it has not met the browser coverage expected; however, support for WebTransport alongside WebSocket is planned after feature completeness. Legacy transport mechanisms, like long-polling, might be interesting to implement but practically doesnt make sense so there is no plan to implement.

### Authentication

WebSocket does not officially allow for any headers besides `Sec-WebSocket-Protocol`, which is used for sub-protocol negotiation, thus a different authentication mechanism than bearer token in the `Authorization` header was needed. The solution was a ticketing system. An initial `GET` request is sent to `/api/realtime/ticket` returns a real-time ticket code that can be used as a query parameter in the WebSocket request to authenticate the connection. First, the user's bearer token is sent in the `GET /api/realtime/ticket` request, server verifies the bearer token and extracts the profileId from the `sub` claim. Then a ticket is created with a random alpha-numeric code of length 8 and the extracted profileId. The ticket is saved in Redis with a time to live (ttl) of 30 seconds. 30 seconds was chosen as long enough that even slow connections should be able to authenticate but short enough that an attack is unlikely and the code is deleted from Redis after authentication to mitigate replay attacks. When the initial WebSocket connection is requested by the client, the code can be provided in the `ticket` query parameter where the server can extract it in a handshake interceptor before finalizing the WebSocket connection. The benefit to this ticket system is that there are no unauthenticated WebSocket connections. An alternative approach would to provide the bearer token inside the first message of an unauthenticated connection and then the server can verify the token and then mark that connection as authenticated, but this leaves the gateway open to Denial of Service (DoS) attacks because clients can created unlimited unauthenticated connections for brief periods of time, and it adds complexity on the server because you have to keep track of authenticated and unauthenticated connections separately.

```mermaid
sequenceDiagram
    Client->>Server: Bearer Token
    Server->>Server: Verify Bearer Token
    Server->>Server: Generate Real-Time Ticket Code
    Server->>Redis: SET generatedCode profileId EX 30
    Server->>Client: {code: generatedCode, ttl: 30s}
    Client->>Server: ws://relay-gateway/ws?ticket=code
    Server->>Redis: GET code
    Redis->>Server: profileId
    Server->>Redis: DEL code
    Server<<->>Client: Authenticated WebSocket connection
```

### Events

Events sent from server to client contain the an operation code, event type, sequence number, and payload. The operation code indicates the operation, whether that be `DISPATCH`, `HEARTBEAT`, `DISCONNECT`, `RESUME`, etc. The event type is used for the `DISPATCH` operation and tells the client what has changed (`MESSAGE`, `WORKSPACE`, etc) and how it has changed (`CREATE`, `UPDATE`, `DELETE`). The sequence code is resuming a connection (TCP already gaurentees no dropped messages), the sequence code can be provided by the client in a `RESUME` operation to notify the gateway that it needs to retrieve the events since then. The payload is a generic field that contains the actual data about the create, update, delete, etc. The payload can be of any shape.

#### Example Event

```json
{
    "op": 0,
    "d": {
        "author": {
            "profileId": "e57307e4-d8f6-4738-9c34-f863115c8aa1",
            "username": "andrew"
        },
        "content": "This is an example",
        "roomId": "8d58a129-3b6a-4a0b-8520-a4c6669991ac"
    },
    "s": 21,
    "t": "MESSAGE_CREATE"
}
```

#### Server to Client Events

Events are sent from server to client with the `DISPATCH` opcode (opcode `0`). Examples of event types include `MESSAGE_CREATE`, `PRESENCE_UPDATE`, `ROOM_DELETE`. Any action that another user can take and your client needs to reflect immediate, has an event type. A full list of events types can be found in the API documentation.

#### Client to Server

Clients may send events to the server to resume a previous session, update the presence status, send heartbeats, etc. Relay does not support sending requests over the real-time connection, i.e. clients may not send messages to a room over their WebSocket connection, that is strictly a REST API request. The thought process with that decision is its easier to gaurentee a message has been sent if we have confirmation from a regular `POST` request rather than send a real-time event and wait for a real-time event back with the message contents. This may change in the future as a big advantage of everything going over the WebSocket connection would be minimal authentication and TLS handshakes, potentially improving throughput.

### Sessions

A session is initiated when the client requests a WebSocket connection with the server. After the WebSocket connection is created, the server send a `READY` event with data that the client needs to render the initial view and a sessionId used for resuming. After the `READY` event, the session has been created and events will begin to send the incrementing sequence number. A heartbeat is used to keep sessions alive, a `heartbeatInterval` is sent during initialization which dictates how often to send the heartbeat in milliseconds.

#### Heartbeat & Heartbeat Ack

Heartbeats are used to eliminate stale sessions. If a session has not recently received a heartbeat, the session is destroyed preventing a resume. After the WebSocket connection is created, the gateway will send a `HELLO` event that contains the `heartbeatInterval`, the interval in milliseconds that the client should be sending a heartbeat using the `HEARTBEAT` event. After the client sends a heartbeat, it should expect a `HEARTBEAT_ACK` event from the server, indicating that the server has acknowledged the heartbeat. The only data sent in the `HEARTBEAT` event is the last sequence number received and the `HEARTBEAT_ACK` event contains no data.

##### Example `HELLO`

```json
{
    "op": 10,
    "d": {
        "heartbeatInterval": 30000
    }
}
```

##### Example `HEARTBEAT`

```json
{
    "op": 1,
    "d": 456
}
```

##### Example `HEARTBEAT_ACK`

```json
{
    "op": 11
}
```

#### Resume

If a client disconnects for any reason, they can reinitialize a connection and send a `RESUME` event with their previous sessionId and sequence number to resume their previous session and retrieve any events sent since the disconnect.

##### Example `RESUME`

```json
{
    "op": 6,
    "d": {
        "sessionId": "8f434f7b-e9d2-4e33-af3a-ec389cfcdaec",
        "s": 456
    }
}
```

#### Example Session Initialization

```mermaid
sequenceDiagram
    Client<<->>Server: Authenticated WebSocket connection
    Server->>Client: HELLO
    loop Every heartbeatInterval ms
        Client->>Server: HEARTBEAT
        Server->>Client: HEARTBEAT_ACK
    end
    Server->>Client: READY
```

### Presence & Fanout

Presence and fanout are important aspects of any real-time platform, thus they are defined in their own documents to prevent too much domain knowledge from leaking into the gateway. The gateway should be dumb and purely infrastructure with no knowledge of what the application itself is doing.
