package com.test

import android.graphics.Typeface
import android.os.Bundle
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.barservicegam.app.R
import com.lib.Utils


class WebActivity : AppCompatActivity() {
    lateinit var webNews: WebView
    lateinit var txtTitle: TextView

    fun loadWebview() {
        txtTitle = findViewById(R.id.txtTitle)
        webNews = findViewById(R.id.webNews)

        val typeface = Typeface.createFromAsset(
            assets,
            "fonts/sfuitextitalic.ttf"
        )
        txtTitle.setTypeface(typeface)

        var content = "<main class=\"body cms-body column main-content\"> <p> <p> </p> </p> <table align=\"center\" class=\"picture\"> <tbody> <tr> <td class=\"pic\"> <img src=\"https://cdntm.24hstatic.com/2021/8/17/8/9199178f51f4a3cb17a38f1e0f53c32f.jpg\" width=\"100%\"/> </td> </tr> <tr> <td class=\"caption\"> <p>Ảnh minh họa: Internet</p> </td> </tr> </tbody> </table> <p> <p> </p> </p> <p>Phó Thủ tướng Lê Minh Khái yêu cầu Bộ Tài chính thực hiện nghĩa vụ trả nợ khoản vay hiệp định đã ký với nước ngoài theo đúng quy định Luật Quản lý nợ công và pháp luật có liên quan, không để ảnh hưởng đến uy tín của Chính phủ. </p> <p>Bộ Giao thông vận tải (GTVT) sớm bàn giao Dự án cho UBND TP. Hà Nội theo quy định để có nguồn trả nợ vay lại của Dự án theo cơ chế tài chính dự án. Trong khi Dự án chưa bàn giao, đến hạn phải trả nợ cho vay lại nhưng chưa có nguồn trả nợ cho Bộ Tài chính, Bộ GTVT cần chủ trì, phối hợp với các cơ quan liên quan kịp thời báo cáo Chính phủ, Thủ tướng Chính phủ. </p> <p>Về lãi phạt chậm trả Bộ Tài chính (Quỹ Tích lũy trả nợ), Bộ GTVT, Ban QLDA thực hiện trả nợ theo hợp đồng cho vay lại đã ký và theo quy định pháp luật hiện hành. Trường hợp phát sinh lãi phạt chậm trả và chưa có nguồn thanh toán, Bộ GTVT chủ trì, phối hợp với Bộ Tài chính và các cơ quan liên quan báo cáo cấp có thẩm quyền xem xét, quyết định. </p> <p> <p> </p> </p> <p>Về giải ngân vốn cho vay lại, hiện nay, do Dự án chưa hoàn thành, hạn mức vay vẫn còn, trong thời hạn hợp đồng vay với nước ngoài, có khối lượng đã thực hiện, do đó, nếu đủ điều kiện giải ngân theo quy định pháp luật thì Bộ Tài chính tiếp tục thực hiện giải ngân phần vốn vay lại của Dự án để thanh toán cho nhà thầu theo quy định. Trường hợp có khó khăn, vướng mắc vượt thẩm quyền, Bộ Tài chính thống nhất với Bộ GTVT, Bộ Kế hoạch và Đầu tư và cơ quan liên quan kịp thời đề xuất cấp thẩm quyền xem xét, giải quyết, bảo đảm khả thi, không ảnh hưởng đến tiến độ dự án theo đúng hợp đồng đã ký kết và quy định pháp luật. Bộ GTVT không để xảy ra sai sót trong việc giải ngân mới. </p> <p> <p> </p> </p> </main>"
        content = Utils.embeddStyleLOADHMTL(content)

        webNews.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
    }
}