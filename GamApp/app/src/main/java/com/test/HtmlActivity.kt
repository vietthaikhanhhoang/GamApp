package com.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.customadapter.DetailNewsAdapter
import com.customadapter.ListNewsAdapter
import data.dataHtml
import org.json.JSONArray
import org.jsoup.Jsoup

class HtmlActivity : AppCompatActivity() {
    lateinit var rclView:RecyclerView
    lateinit var detailNewsAdapter:DetailNewsAdapter
    var arrayContent = ArrayList<dataHtml>()

    fun parseContent() {
        val strHtml = "<img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/041d147783bd4e051690464bfbf26c29.png\\\" width=\\\"100%\\\"><em> </em> <p>Trước khi lên sóng truyền hình, dù nhân vật chính có là ai, là nghệ sĩ hay doanh nhân thì vẫn được trang điểm một lớp nhẹ phía sau hậu trường. Khi đó, hình ảnh lên sóng sẽ tươi tắn hơn, trẻ trung hơn.</p><p>Các &#34;cá mập&#34; lên sóng chương trình Shark Tank Việt Nam (Thương vụ bạc tỷ) cũng không nằm ngoại lệ. Họ được ekip phía sau hỗ trợ từ kịch bản đến trang phục cũng như make-up sao cho hài hòa với sóng truyền hình. </p><p>Ấy vậy mà, trong buổi livestream trực tiếp chia sẻ với người hâm mộ nằm trong khuôn khổ chương trình Shark Tank Việt Nam mùa 4, Shark Nguyễn Hòa Bình đã tự tin công khai mặt mộc của mình. Thực ra, lý do không phải Shark Bình cố ý muốn vậy để gây chú ý đâu! Mà bởi CEO của NextTech... đi muộn nên đành bất chấp để mặt mộc trước mấy nghìn người hâm mộ.</p><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/f93f18d937ce36561a188c24e8f05f58.jpg\\\" width=\\\"100%\\\"><em></em><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/c9df596d9a7f868c729b3efd5847a1e3.jpg\\\" width=\\\"100%\\\"><em></em><p>Biết mình đã muộn giờ nhưng Shark Bình vẫn giữ phong thái tự tin, chuyên nghiệp trước máy quay và gửi lời chào cũng như lời xin lỗi tới quý khán giả:<em> &#34;Xin chào các bạn, xin lỗi để các bạn chờ lâu nhé. Hôm nay đường kẹt xe quá nên là đến phim trường hơi muộn, làm cho mình chưa kịp makeup gì hết&#34;.</em></p><p>Vì lí do tắc đường, đến trường quay muộn nên Shark Bình quyết định &#34;vào việc&#34; luôn để khán giả không phải đợi. Anh cũng nói luôn với người hâm mộ là anh đang để mặt mộc. Nhưng khán giả lại không tin vào điều đó và liên tục hỏi:<em> &#34;Có phải Shark chưa makeup thật không?&#34;, &#34;Chưa makeup mà sao da Shark lại láng mịn thế?&#34;...</em></p><p>Thì ra, Shark Bình đã nhờ đến phần mềm chỉnh mặt để thật tự tin để lên sóng. Sau đó, anh thậm chí còn hướng dẫn người xem livestream sử dụng phần mềm từ bóp cằm, thon mặt, gọn mũi, mắt to... vô cùng thuần thục.</p><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/ee4d29edda3c1f857998712e4584d045.jpg\\\" width=\\\"100%\\\"><em></em><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/ba984b8b5df7600ad665df4a761d1db5.jpg\\\" width=\\\"100%\\\"><em></em><p>Shark Bình không hổ là Shark công nghệ, phải không? Ngoài đời, các &#34;cá mập&#34; có nhiều cơ hội hơn để thể hiện những tính cách đáng yêu trước người hâm mộ. Một lần khác, Shark Bình còn vô tư... đánh son khi livestream.</p><p>Chả là, nhân dịp học viện dạy livestream bán hàng online của mình ra mắt thị trường TP. Hồ Chí Minh vào cuối tháng 3, Shark Bình đã thực hiện thử thách tự mình livestream bán hàng. Tuy nhiên, vì cây son không phải đồ vật thân thuộc nên CEO của NextTech khá lúng túng khi cầm nó trên tay.</p><p>Khi nhận được sự trợ giúp của chuyên gia livestream cùng, Shark Bình cũng không tự tin nên son đánh lên môi có phần bị lem ra ngoài. Dù khá ngại ngùng nhưng sau khi được tô son, anh bặm môi để son đều hơn.</p><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/31d120b987dae08b9d66ad355620ed6a.png\\\" width=\\\"100%\\\"><em></em><img src=\\\"https://cdntm.24hstatic.com/2021/7/24/7/0043e0d347494c040d86147fa37f4ce3.jpg\\\" width=\\\"100%\\\"><em></em><p>Còn dưới đây là những hình ảnh hậu trường của Shark Nguyễn Hòa Bình trong chương trình Shark Tank Việt Nam. Dù trang điểm giúp Shark Bình trở nên điển trai hơn nhưng những màn &#34;chốt deal&#34; của Shark vẫn thật ấn tượng, phải không?</p><video controls=\\\"controls\\\" id=\\\"fplayVideo0\\\" onclick=\\\"showVideoArticle(&#39;https://cafebiz.cafebizcdn.vn/162123310254002176/2021/7/23/GyUdxcMo3Ac-1627034336103214554525.mp4&#39;)\\\" width=\\\"100%\\\"></video><script>var videoSource = \\\"https://cafebiz.cafebizcdn.vn/162123310254002176/2021/7/23/GyUdxcMo3Ac-1627034336103214554525.mp4\\\";var listVideo = videoSource.split(\\\"***\\\");var videoCount = listVideo.length;var i=0;document.getElementById(\\\"fplayVideo0\\\").setAttribute(\\\"src\\\",listVideo[0]);function videoPlay(videoNum)   {document.getElementById(\\\"fplayVideo0\\\").setAttribute(\\\"src\\\",listVideo[videoNum]);document.getElementById(\\\"fplayVideo0\\\").load();document.getElementById(\\\"fplayVideo0\\\").play();}document.getElementById('fplayVideo0').addEventListener('ended',myHandler,false);function myHandler(){i++;if(i < videoCount){videoPlay(i);}}</script><p>Cô giáo Vật lý &#34;triệu view&#34; là ai mà đến cả PewPew cũng thức đến 12h đêm nghe giảng bài?</p> <strong>PV</strong>"
        val doc = Jsoup.parse(strHtml)

        arrayContent.add(dataHtml("Bài báo hay nhất tôi gặp", "title"));
        arrayContent.add(dataHtml("Tổng hợp", "category"));
        arrayContent.add(dataHtml("Tóm tắt nội dung", "desc"));

        for (i in 0 until doc.body().children().size) {
            var element = doc.body().children()[i]
            if(element.tagName() == "p") {
                if(element.children().size > 0) {
                    var text = element.html()
                    if(text.length > 0) {
                        var content = text
                        var type = "textHtml"
                        arrayContent.add(dataHtml(content, type))
                    }
                } else {
                    var text = element.text()
                    if(text.length > 0) {
                        var content = text
                        var type = "text"
                        arrayContent.add(dataHtml(content, type))
                    }
                }
            }
//            else if (element.tagName() == "em") {
//                var text = element.html();
//                if (text.length > 0) {
//                    var content = text;
//                    var type = "underline";
//                    arrayContent.add(dataHtml(content, type));
//                }
//            }
//            else if (element.tagName() == "img") {
//                var src = element.attr("src")
//                if (src != null) {
//                    var type = "img";
//                    var content = src;
//                    arrayContent.add(dataHtml(content, type));
//                }
//            }
        }

//        Log.d("vietnb", "content boc tach: " + arrayContent.toString())

//        detailNewsAdapter = DetailNewsAdapter(arrayContent)
//        rclView.adapter = detailNewsAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)

        rclView = findViewById(R.id.rclView)
        var mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rclView.layoutManager = mLayoutManager

        parseContent()
    }
}