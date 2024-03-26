# AEM Rockstar 2024 - ChatAEM Integration Code
AEM as a Cloud Service code for the ChatAEM integration in teh AEM authoring interface.
The codebase is not a fully fledged AEM project but an overlay which you can apply on your own project and roll out with your onw codebase.
It contains the necessary clientlib, component, java code and WCM overlay to integrate the ChatAEM interface into an AEM Author instance, as a sidebar extension.

# How to build
The code can be build using maven.

    mvn clean install


# Integration

Apply the clientlibs, the overlay, component and javacode to you onw project and make sure that the following env variables are available in your environment:

| Variable    | Type | Description |
| -------- | ------- | ------- |
| CUSTOM_ENV_CHATAEM_HMAC_KEY  | secret  | HMAC key used to sign the JWT used to authorize the ChatAEM session. Must be in sync with cloudfront keyvaluestore the ChatAEM infra |
| CUSTOM_ENV_CHATAEM_URL | env     | Public facing URL of the ChatAEM infrastructure. Can be configured in the infra project. |

# Dependencies

Make sure to have the following projetcs rolled out:

| Project  | Description |
| -------- | ------- | 
| [ChatAEM Client App](https://github.com/thomashartm/rockstar-2024-client)| ChatAEM RAG application and LLM integration API. |
| [ChatAEM Terraform Infrastructure](https://github.com/thomashartm/rockstar-2024-client) | Provides the necessary AWS infrastructure. |
| [ChatAEM Embeddings](https://github.com/thomashartm/rockstar-2024-embed) | Embeddings generation scripts |


