# Configuration screens

ukulib provides multiple ways of easily creating screens to manage your mod's configuration. These screens try to stay as inline as possible with what you can find in the vanilla game, while trying to provide a better API.

!!! todo

    add screenshots

## Common concepts

Essentially, a config screen is composed of a list of [`WidgetCreator`](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib-common/latest/.cache/unpack/net/uku3lig/ukulib/config/option/WidgetCreator.html)'s. Each one (usually) represents and "wraps" a single value in your configuration, and will display a widget that allows editing the value in the most appropriate manner. ukulib provides a bunch of them already, which should cover the vast majority of cases.

Each `WidgetCreator` will have a different constructor signature but they all have 3 parameters in common:

- a translation key, which should translate to something that describes in a fairly concise manner what the option does (eg. `#!java "betterhurtcam.config.enabled"` which translated to `Hurtcam enabled`)
- a value, which will serve as the initial value for the widget when the screen is shown (eg. `#!java config.enabled` or `#!java config.isEnabled()`)
- a setter, usually a lambda for convenience (eg. `#!java (v) -> config.enabled = v` or `#!java config::setEnabled`),
- (optional) a tooltip, if you have a complicated option and want to display more information.
