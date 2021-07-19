package preselection

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PreselectionSimulation extends Simulation {

    val users = Integer.getInteger("users", 1)
    val ramp = Integer.getInteger("ramp", 1)
    val repetition = Integer.getInteger("repetition", 1)
    println("repetition = " + repetition)
    val sleeptime = Integer.getInteger("sleeptime", 1)
    println("sleeptime = " + sleeptime)

    object SaisieFormulairePreselection {
        val headers_0 = Map(
            "Accept-Encoding" -> "gzip, deflate",
            "Cache-Control" -> "no-cache",
            "Pragma" -> "no-cache",
            "Proxy-Connection" -> "keep-alive")
    
        val headers_1 = Map(
            "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "Cache-Control" -> "max-age=0",
            "Sec-Fetch-Dest" -> "document",
            "Sec-Fetch-Mode" -> "navigate",
            "Sec-Fetch-Site" -> "none",
            "Sec-Fetch-User" -> "?1",
            "Upgrade-Insecure-Requests" -> "1",
            "sec-ch-ua" -> """" Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"""",
            "sec-ch-ua-mobile" -> "?0")
    
        val headers_2 = Map(
            "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "Cache-Control" -> "max-age=0",
            "Content-Type" -> "multipart/form-data; boundary=----WebKitFormBoundaryL18LE4IKR2JZp4Nw",
            "Origin" -> "https://redcap.dev.valeria.science",
            "Sec-Fetch-Dest" -> "document",
            "Sec-Fetch-Mode" -> "navigate",
            "Sec-Fetch-Site" -> "same-origin",
            "Sec-Fetch-User" -> "?1",
            "Upgrade-Insecure-Requests" -> "1",
            "sec-ch-ua" -> """" Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"""",
            "sec-ch-ua-mobile" -> "?0")
    
        val headers_3 = Map(
            "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "Sec-Fetch-Dest" -> "document",
            "Sec-Fetch-Mode" -> "navigate",
            "Sec-Fetch-Site" -> "same-origin",
            "Sec-Fetch-User" -> "?1",
            "Upgrade-Insecure-Requests" -> "1",
            "sec-ch-ua" -> """" Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"""",
            "sec-ch-ua-mobile" -> "?0")
    
        val uri2 = "http://www.gstatic.com/generate_204"
    
        val saisie = repeat(repetition.toInt, "i") {
            exec(http("request_0")
                .get(uri2)
                .headers(headers_0))
            .pause(1)
            .exec(http("request_1")
                .get("/surveys/?s=FA4939J3FA")
                .headers(headers_1))
            .pause(1)
            .exec(http("request_2")
                .post("/surveys/index.php?s=FA4939J3FA")
                .headers(headers_2)
                .body(RawFileBody("preselection/basicsimulation/0002_request.dat")))
            .pause(1)
            .exec(http("request_3")
                .get("/surveys/index.php?__closewindow=1&pid=233")
                .headers(headers_3))
        }.pause(sleeptime)
    }

    val httpProtocol = http
        .baseUrl("https://redcap.dev.valeria.science")
        .inferHtmlResources()
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.9")
        .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")

    val participants = scenario("Participants").exec(SaisieFormulairePreselection.saisie)
    setUp(
            participants.inject(rampUsers(users).during(ramp))
                .pauses(uniformPausesPlusOrMinusPercentage(50))
        ).protocols(httpProtocol)
}
