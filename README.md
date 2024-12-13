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

# Customization

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

For details on parameters, see "Note #5" in the source code.
