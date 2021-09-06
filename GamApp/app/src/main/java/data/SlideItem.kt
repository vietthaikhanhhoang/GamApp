package data

class SlideItem {
    private var image: Int = 0

    constructor(image: Int) {
        this.image = image
    }

    public fun getImage(): Int{
        return image
    }
}