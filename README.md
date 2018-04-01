# Trunk
[![](https://jitpack.io/v/MrGraversen/Trunk.svg)](https://jitpack.io/#MrGraversen/Trunk)
[![Codeship Status for MrGraversen/Trunk](https://app.codeship.com/projects/8b089450-0ba0-0136-744c-56d424be27fe/status?branch=master)](https://app.codeship.com/projects/281908)

A growing collection of classes I've written and found useful in any degree. I'm (going to be) using this as a base toolbox for most projects; perhaps you may find it of use as well.

## Installation

You may use JitPack to install this from the GitHub releases.  
Add the following to your `pom.xml` if using Maven (click the little JitPack badge for other build systems):

```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

```
<dependency>
	<groupId>com.github.MrGraversen</groupId>
	<artifactId>Trunk</artifactId>
	<version>LATEST</version>
</dependency>
```

## Packages

### `console`
Utilities related to console in- and output.

### `fx`
Helps mending some JavaFX pain.

### `hardware`
Handles aggregation of hardware-related information, using the [OSHI project](https://github.com/oshi/oshi).

### `hashing`
Some wrappers of the `java.security` functionality.

### `io`
Anything related to IO; also serialisation. Wrapping of `java.nio` to allow for more ease-of-use.

### `os`
*"Write once, run anywhere"* - but provide no easy way of determining the underlying operating system. That's fixed now.

### `password`
Password generator utils, also exposes password strength estimation using [Dropbox' Low-Budget Password Strength Estimation algorithms](https://github.com/dropbox/zxcvbn).

Also has BCrypt functionality, including an check-and-update method, for all your Moore's law scalability needs!

### `random`
Generate random stuff, even using `/dev/urandom` if you're on \*nix systems!

### `reflection`
Tools related to Java reflection techniques.

## Dependencies

This module also serves as a easy way of bundling together commonly-used dependencies, such as:

* org.mindrot.jbcrypt
* com.google.code.gson.gson
* org.apache.commons.commons-lang3
* commons-codec.commons-codec

Uses JUnit 5.
