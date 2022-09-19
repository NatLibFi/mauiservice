# THIS REPOSITORY IS OBSOLETE.

For Annif the similar functionality as offered by Maui is now available by MLLM backend.

# MauiService

This is a simple REST service for [Maui](https://github.com/NatLibFi/maui) annotations. The REST interface can be configured to host multiple parallel configurations (that is, different models, vocabularies, language etc.).

This service is built using Spring Starter Web and can be ran as a stand-alone Java application or deployed in a servlet container such as Apache Tomcat or Jetty.

Pre-built versions can be found on [Maven Central](https://search.maven.org/search?q=g:fi.nationallibrary). There you can find a WAR file that can be dropped into Tomcat. See the [developer notes](DEVELOPER.md) for more information about how to deploy and configure MauiService.

The REST api exposes one endpoint per configuration /maui/[configuration-id]/analyze . Note that the /maui part is defined by how the service is hosted. If the service is installed to a servlet container (like Tomcat) as mauiservice.war, the path will be /mauiservice/[configuration-id]/analyze

Also, /mauiservice returns a JSON file with a list of configurations, and /maui/[configuration-id] will return a JSON file with a list of services provided for that configuration (at the moment just "analyze").

The service is developed for the National Library of Finland by Spatineo (Sampo Savolainen).


