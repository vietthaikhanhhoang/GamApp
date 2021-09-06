package data

class dataHtml {
    var content: String = ""
    var type: String = ""
    var bg: Boolean = false

    constructor(content: String, type: String) {
        //print("content: " + content + " | type: " + type);
        this.content = content
        this.type = type
    }

    constructor(content: String, type: String, bg: Boolean) {
        //print("content: " + content + " | type: " + type + " | " + bg);
        this.content = content
        this.type = type
        this.bg = bg
    }
}