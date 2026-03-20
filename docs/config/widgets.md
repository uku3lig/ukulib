# Widgets

You can find a [list of all the available widgets](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib-common/latest/.cache/unpack/net/uku3lig/ukulib/config/option/package-summary.html) in the javadoc. Most stuff should be self-explanatory thanks to the already existing javadoc comments; this page details more complex use cases and widgets.

## `CyclingOption`

`CyclingOption` is one of the more complex widget types due to the multitude of ways it can be used in. The three main usages are:

- A boolean toggle: this is the simplest one, they are constructed with the `ofBoolean` methods and just take in the "usual" parameters.
- Choosing an enum value: this (obviously) only works with actual enums, so that the values can be fetched automatically, you therefore have to pass the class of your enum and (optionally) a function that converts enum variants to human-friendly text. Constructed using `ofEnum`.
- Choosing an generic object: the most generic of the three, takes in a list of `T` and makes the "value-to-text" function mandatory. Constructed via the class constructor.

!!! tip

    Creating a "value-to-text" function can be cumbersome and annoying for most use cases, which is why `CyclingOption` also provides `ofTranslatableEnum` and `ofTranslatable` (for generic objects) functions. These require your items to implement [`StringTranslatable`](https://maven.uku3lig.net/javadoc/releases/net/uku3lig/ukulib-common/latest/.cache/unpack/net/uku3lig/ukulib/config/option/StringTranslatable.html), which associates each value with an internal string identifier and a translation key.

    *Example:*

    ```java
    @Getter
    @AllArgsConstructor // (1)!
    public enum OffhandSlotBehavior implements StringTranslatable {
        ALWAYS_IGNORE("always_ignore", "armorhud.option.alwaysIgnore"),
        ADHERE("adhere", "armorhud.option.adhere"),
        ALWAYS_LEAVE_SPACE("always_leave_space", "armorhud.option.alwaysLeaveSpace");

        private final String name;
        private final String translationKey;
    }
    ```

    1. I use [Lombok](https://projectlombok.org) here which helps writing less boilerplate; this example would work perfectly fine by writing the constructor and overriding the methods by hand.

## `InputOption` and validation

ukulib provides a few widgets for text input, namely `InputOption`. It's fairly simple at its core, taking in a translation key, an initial value and a setter.

However, its behavior can be fine-tuned with two more options: `maxLength` (self-explanatory) and a validator. This validator is a function that takes in the current value of the input and returns a boolean, `true` if the value is valid. This allows for check for virtually anything, eg. if the value is alphanumeric, if it's a valid UUID, etc.

Having a validation function set will prevent the user from saving their changes to the config if the value they put in is invalid, by disabling the "Done" button and showing a tooltip indicating what's wrong. (Note: it currently isn't possible to customize the message displayed in the tooltip). The screen can also be forcibly exited by pressing <kbd>Esc</kbd>, but modifications will not be saved.

## Interactive position selection

## Wide widgets
