# Relay Profile Image Upload Design

## Overview

Relay allows users to upload a profile image.

## Background & Motivation

Users expect the ability to upload personalized profile images to display by their messages and under their user profile page. Users also expect a seamless experience that "just works", our goal is always to provide this experience with as little perceived latency as possible.

## Design

### Happy Path

1. User selects an image from their filesystem with a max size of 5mb to upload as their profile image and user is prompted to crop and size their chosen image
2. In the background while the user is cropping their image, the client is making a request to retrieve a presigned put object request to the storage appliance with the key 'temp/{profile-uuid}/original' in the 'relay-temp-profile-images' bucket, and then making the PUT request to upload the original image
3. After the user has selected cropping and size, that information is sent to the server with a POST /api/profile/image and the server publishes a ProfileImageUpdated event
4. Storage service consumes the ProfileImageUpdated event andretrieves the image from the storage appliance and performs the crop modification to the image, generates a 128x128 and 512x512 variant and places them in the storage appliance within the 'relay-profile-images' bucket with the key 'avatar/{profile-uuid}/128x128' and 'avatar/{profile-uuid}/512x512' respectively
5. After successful upload, a ProfileImageCompleted event is published and the Notification Director sends a message to the client over WebSocket with the public urls that the client can use to retrieve the new profile images and display success

### Edge Cases & Alternative Flows

All edge cases are handled by the 24 hour retention policy in the 'relay-temp-profile-images' bucket, such as user abandonment.

## Fault Handling

| Fault                                  | Remediation                                      | User-Facing Message |
| -------------------------------------- | ------------------------------------------------ | ------------------- |
| Client-side image upload failure       | Display error message                            | Try again later     |
| Server-side image retrieval failure    | Return and display error message                 | Try again later     |
| Server-side image modification failure | Send WebSocket message and display error message | Try again later     |
| Server-side image upload failure       | Send WebSocket message and display error message | Try again later     |
| Invalid crop parameters from client    | Return and display error message                 | Try again           |

## Security Considerations

Requests to retrieve a presigned put object url are to be authenticated using the existing token mechanism, and the UUID used in the key is to be retrieved from the token. Profile images are inherently public data so retrieval of profile images are not to be authenticated.

## Technologies & Dependencies

| Technology                 | Purpose                                 |
| -------------------------- | --------------------------------------- |
| Garage                     | Storage appliance                       |
| libvips & vips-ffm         | Image modification                      |
| Notification Director      | Server to client WebSocket coordination |
| Storage service (internal) | S3 compliant storage interface          |
