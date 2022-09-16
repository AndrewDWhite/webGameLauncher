# webGameLauncher
An early in progress open source implementation of a web based game launcher supporting galaxy's api. Currently only some of the api calls that galaxy makes to plugins are implemented (enough to startup the generic plugin and retrieve data from an already existing installation). Early controller support is implemented for firefox.

## Running the launcher
First start the launcher's main java function. While the launcher is running you can run, currently one, plugin manually and have that plugin provide data to the launcher. You can start up the [generic plugin](https://github.com/AndrewDWhite/GalaxyGenericImporterPlugin) with the following command.
`py -3.7-32 generic.py token 8488`


<img width="894" alt="user interface" src="https://user-images.githubusercontent.com/972757/190617998-fb50106d-ec96-42c2-b9a6-7921df6cbce4.png">
