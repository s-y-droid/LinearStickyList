# LinearStickyList
[![](https://jitpack.io/v/s-y-droid/LinearStickyList.svg)](https://jitpack.io/#s-y-droid/LinearStickyList)

LinearStickyList is a simple StickyHeader UI component based on ScrollView and LinearLayout.
It scrolls quickly because there is no rebinding that causes lag when scrolling from off-screen to on-screen, as with RecyclerView.
It is best suited for UI requirements where the number of elements in the list is small(at most 20) and many Views must be placed in each element.

![a](https://github.com/user-attachments/assets/1f3a52bf-3e7a-427e-a4a0-bd1ae1d831d1)

# Integration
Add the following to the build.gradle file in the project root:
```
repositories {
  maven("https://jitpack.io")
}
```
Add the following to your application's build.gradle file:
```
implementation("com.github.s-y-droid:LinearStickyList:v1.0.2")
```

# Usage

<b>Step 1:</b> Place the LinearStickyListFragment on your screen and call the setup() method.
```kotlin
linearStickyListFragment.setup(
    list = listOf(
        P1CellFragment.newInstance(),
        P2CellFragment.newInstance(),
        // etc..
    )
)
```
The list specifies all the Fragments that make up the LinearStickyListFragment.

<b>Step 2:</b> Implement the cell fragment by inheriting LinearStickyListCellFragmentBase.
```kotlin
class P1CellFragment : LinearStickyListCellFragmentBase() {

    companion object {
        fun newInstance() = P1CellFragment()
    }

    override fun isStickyHeader() = true
    
    // etc..
}
```
Override isStickyHeader(). If you want to make the cell a StickyHeader, set it to true.

For more details, search the source code for "Note #".

[Note #1 About setup of LinearStickyListFragment](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/MainActivity.kt#L36)

[Note #2 How can a parent safely obtain an instance of a Cell fragment?](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/MainActivity.kt#L101)

[Note #3 About Cell fragment implementation](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/P1CellFragment.kt#L27)

[Note #4 How to emit an event from a Cell fragment to its parent](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/P1CellFragment.kt#L70)

[Note #5 Customizing the scrollbar](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/MainActivity.kt#L53)

# Customization

## Scrollbar
When calling the setup() method of the LinearStickyListFragment, you can specify scrollbar options.
```kotlin
data class LinearStickyListScrollbarOptions(
    val isShowScrollbar : Boolean = true,
    val widthDp : Float = 4.0f,
    val drawableResId : Int = R.drawable.linear_sticky_list_defalut_scrollbar,
    val isFadeOut : Boolean = true,
    val fadeOutAlphaAnimationTimeMs : Long = 600L,
    val fadeOutInactivityTimeMs : Long = 1200L
)
```
If you do not specify any options, a design and functionality similar to ScrollView will be displayed.
For details on parameters, see ["Note #5"](https://github.com/s-y-droid/LinearStickyList/blob/master/app/src/main/java/com/example/stickyheader/MainActivity.kt#L53) in the source code.

## Triggers when a Cell enters or leaves the screen.

LinearStickyListCellFragmentBase has an onDistanceFromDisplayArea() method that can be optionally overridden.
```kotlin
class P1CellFragment : LinearStickyListCellFragmentBase() {

    override fun onDistanceFromDisplayArea(
        isOnScreen: Boolean,
        distancePx: Float
    ) {
        // Implementing trigger detection to free memory and stop/resume animation
    }
}
```
This method is called back from LinearStickyListCellFragment.
```
isOnScreen : Whether the cell is visible on screen or not
distancePx :　If isOnScreen is false, the distance away from the screen (unit: Px)
```
A callback will be made to tell you how far it is from the screen.
Its purpose is to stop the animation of off-screen Cell or to free memory, etc.
Please override if you need it.
