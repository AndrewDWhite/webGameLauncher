# webGameLauncher
An early in progress open source implementation of galaxy. Currently only some of the calls that galaxy makes to plugins are implemented (enough to startup the generic plugin and retrieve data from an already existing version).
## Running the launcher
First start the launcher's main java function. While the launcher is running you can run, currently one, plugin manually and have that plugin provide data to the launcher. You can start up the [generic plugin](https://github.com/AndrewDWhite/GalaxyGenericImporterPlugin) with the following command.
`py -3.7-32 generic.py token 8488`
