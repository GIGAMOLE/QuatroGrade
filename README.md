![](/media/intive_3.jpeg)

![](https://jitpack.io/v/GIGAMOLE/QuatroGrade.svg) | [Setup Guide](#setup) | [Report new issue](https://github.com/GIGAMOLE/QuatroGrade/issues/new)
# QuatroGrade

`QuatroGrade` is a beautiful multi color gradient. Basically, it is a 4 vertex gradient.

![](/media/preview-loop.gif)

Features:  
- Multi color, or the default 4 corners color gradient `QuatroGradeView`.
- Changing the orientation of the gradients for better look.
- Gradient colors dynamic change and animation support.
- XML and programmatic setup.
- Advanced Sample App.   

## Sample App

| Default | Custom | Animation |
|-|-|-|
| ![](/media/default.gif) | ![](/media/custom.gif) | ![](/media/animation.gif) |

Download or clone this repository to discover the sample app. 

## Setup

Add to the root `build.gradle`:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add to the package build.gradle:

```groovy
dependencies {
    implementation 'com.github.GIGAMOLE:QuatroGrade:{latest-version}'
}
```

Latest version is: ![](https://jitpack.io/v/GIGAMOLE/QuatroGrade.svg). 

Also, it is possible to download the latest artifact from the [releases page](https://github.com/GIGAMOLE/QuatroGrade/releases).

## Guide

#### XML Guide:

`QuatroGradeView` tag:

```xml
<com.gigamole.quatrograde.QuatroGradeView/>
```

`QuatroGradeView` attributes:

| Attribute | Description |
|-|-|
| `qgv_colors` | The `array` of color values (size 4). Setup with 4 colors on each corner: top-left, top-right, bottom-left, bottom-right. |
| `qgv_topColors` | The `array` of color values (size 2+). Provides the top gradient colors. |
| `qgv_topPositions` | The `string-array` of float values (size 2+). Provides the top gradient colors positions. |
| `qgv_bottomColors` | The `array` of color values (size 2+). Provides the bottom gradient colors. |
| `qgv_bottomPositions` | The `string-array` of float values (size 2+). Provides the bottom gradient colors positions. |
| `qgv_orientation` | The `enum` of `horizontal` or `vertical` value. Set the orientation of the top and bottom gradients. In the `vertical` orientation the top gradient moves to the left, and bottom moves to the right. |

The `qgv_topColors` and `qgv_topPositions` arrays should have equals size. The same for the `qgv_bottomColors` and `qgv_bottomPositions`. Basically, the color position range from `0.0` to `1.0`.

The `qgv_colors` tag is dominant, so it will override the `qgv_topColors`, `qgv_topPositions`, `qgv_bottomColors`, `qgv_bottomPositions` attributes. 

The `qgv_orientation` change is useful for presenting 3+ colors on the wide or tall screens(depends on the orientation).

#### Code Guide:

Methods to setup the `QuatroGradeView`: `setColors`, `setColorsRes`, `setColorsInt`, `setGrades`.

The `GradeModel` is the holder of the color and its position on the gradient.  

To change the orientation: `quatroGradeView.orientation`.

Methods to animate or change the single `GradeModel`: `getTopGrades`, `getBottomGrades`, `getGrades`.

The `refresh` method is applying changes and redraws gradients after the manipulation over the `GradeModel`s.

Please, follow the [Sample App](#sample-app) to properly discover these features.

## License

MIT License. See the [LICENSE](https://github.com/GIGAMOLE/QuatroGrade/blob/master/LICENSE) file for more details.

## Credits

Special thanks to the [side.codes](https://github.com/side-codes) for the amazing [color picker library](https://github.com/side-codes/andColorPicker). 

Created at [intive](https://intive.com).

![](/media/intive_4.png)

## Author:

[Basil Miller](https://www.linkedin.com/in/gigamole/)  
[gigamole53@gmail.com](mailto:gigamole53@gmail.com)

![](/media/intive_2.png)
