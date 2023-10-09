# reindev_betterchatmod

## What is this?
This is a simple - but customizable - mod for [ReIndev 2.8.1_04](https://reindev.miraheze.org/wiki/Reindev_Wiki),
and for ReIndev's own mod loader called [FoxLoader](https://github.com/Fox2Code/FoxLoader),
that aims to improve on the chat of base ReIndev.
(Which is based of Minecraft Beta 1.7.3)

If you want a command autocompletion feature, please take a look at the amazing [ReIndev Command Autocomplete](https://github.com/jelliedbanana/ReIndev-CommandAutocomplete) mod.

(Currently conflicts with this mod, but we do plan to fix that).
## Okay cool, but... what does it do?
What does it currently do:
- Extended chat logs, default 1024, instead of 50 in base ReIndev.
- A "message history".

  Basically the feature that you have in modern Minecraft,
  with the up and down keys to navigate through your past sent messages & commands.

  By default it will be global in 1.1.0 and prior.
  
  In 1.2.0+ it will be per world/server, but you can change that
  (Cf. the "[How to configure?](#how-to-configure)" section).

- New in 1.1.0: You can use the "home" and the "end" keys to move the cursor at the extremities of the chat text field

- New in 1.2.0:

  - Json-based config system. ([How to configure?](#how-to-configure))
  - Ctrl+backspace/delete to delete a whole word or chunk of characters.
  - Ctrl+left/right arrow to navigate through a whole word or chunk of characters.
  - (Probably more, as I develop this version :3)

## How to install?
Make sure you have [FoxLoader](https://github.com/Fox2Code/FoxLoader) installed.

Preferably version 1.2.17+, for [ReIndev 2.8.1_04](https://reindev.miraheze.org/wiki/Reindev_Wiki).

And then, just put the mod into your "mods" folder, and voilà, enjoy. :)

## How to configure?
WIP