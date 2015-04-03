# RecyclerViewCompat
extensions for RecyclerView, made by laysionqet.

## now support
header view and footer view are supported with HFRecyclerView,
CursorRecyclerAdapter are also supported alone with HFRecyclerView.

## usage
I have tried to make the usage the same with what ListView has offered, 
but you have to be aware that if you try to use HFRecyclerView, the Adapter you are gonna set is about to override a specific method shown below.

```java
@Override
public int getItemViewType(int position) {
    return "what ever >= 1";
}
```
