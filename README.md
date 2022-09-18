# webGameLauncher
An early in progress open source implementation of a web based game launcher supporting [galaxy's plugin api](https://galaxy-integrations-python-api.readthedocs.io/en/latest/) [[src]](https://github.com/gogcom/galaxy-integrations-python-api). This project was created in order to learn how the API works to facilitate creating an integration test for plugin developers.
* Currently only some of the api calls that galaxy makes to plugins are implemented (enough to startup the generic plugin and retrieve data from an already existing installation) and some incorrectly. See the [issues tab](https://github.com/AndrewDWhite/webGameLauncher/issues) for more details.
* It includes some additional features for experimenting on community requested features such as controller support, implemented for firefox, and metaddata pulling from other sources.

## Building the Launcher
The project can be build with maven `mvn clean package` . Update the global variables with your twitch application information, if you want igdb image enrichment.

## Running the launcher
First start the launcher's main java function.
`java -jar target/webGameLauncher-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

While the launcher is running you can run, currently one, plugin manually and have that plugin provide data to the launcher.

You can start up the [generic plugin](https://github.com/AndrewDWhite/GalaxyGenericImporterPlugin) with the following command.
`py -3.7-32 generic.py token 8488`

Once it has loaded you can visit `http://localhost:8080/games` to see your library. You may need to refresh a few times until your library has completely loaded depending on the plugin.

<img width="894" alt="user interface" src="https://user-images.githubusercontent.com/972757/190617998-fb50106d-ec96-42c2-b9a6-7921df6cbce4.png">
