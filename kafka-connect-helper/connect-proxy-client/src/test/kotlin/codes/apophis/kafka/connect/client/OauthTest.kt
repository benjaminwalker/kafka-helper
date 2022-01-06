package codes.apophis.kafka.connect.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources
import io.mockk.*
import io.mockk.impl.annotations.SpyK
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.*
import java.io.IOException
import java.nio.charset.StandardCharsets


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class OauthTest {

    @SpyK
    var mOauthClient = JerseyProxyClient.Companion.Oauth2Client("blah","blah","blah")
    /*
    val apiKey: String,
            val apiSecret: String,
            private val authenticationEndpoint: String
     */
    val oauthResponseFixture = JerseyProxyClient.Companion.OauthResponse(
        "Bearer",
        "YeScdbSMhaHrfiSdHCe3AsZMG58h",
        "8c5d7fc6-4a09-48a5-811b-05cf3a847ec8",
        3599,
        0,
        0,
        1611783653262
    )

//    val fixtureResponse = Response.ok().entity(oauthResponseFixture).build()

    @SpyK
    var mResponse = Response.ok()
        .entity(oauthResponseFixture)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build()

    //executeAuth
    @BeforeAll
    fun beforeAll() {
        MockKAnnotations.init(this)
    }

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testOauthResponseSerDe(){
        val mapper = ObjectMapper()
        // Deserialize what we serialized
        Assertions.assertEquals(oauthResponseFixture, mapper.readValue(mapper.writeValueAsString(oauthResponseFixture),JerseyProxyClient.Companion.OauthResponse::class.java))
        // Deserialize our fixture string and compare to object
        Assertions.assertEquals(oauthResponseFixture,mapper.readValue(fixture("fixtures/oauthresponse.json"), JerseyProxyClient.Companion.OauthResponse::class.java))

    }

    @Test
    fun testAuthenticate200() {
        every { mOauthClient.executeAuth() }.answers { mResponse }
        every { mResponse.readEntity(JerseyProxyClient.Companion.OauthResponse::class.java) } answers {oauthResponseFixture}
        mOauthClient.authenticate().fold({
                                         Assertions.fail()
        },{
            Assertions.assertEquals(oauthResponseFixture, it)
        })

    }

    private fun <T, B> MockKStubScope<T, B>.returns(function: () -> B) {
        mResponse
    }


    /*
  "configs": [
    {
      "definition": {
        "name": "topics",
        "type": "LIST",
        "required": false,
        "default_value": "",
        "importance": "HIGH",
        "documentation": "",
        "group": "Common",
        "width": "LONG",
        "display_name": "Topics",
        "dependents": [],
        "order": 4
      },
      "value": {
        "name": "topics",
        "value": "test-topic",
        "recommended_values": [],
        "errors": [],
        "visible": true
      }
    }]}
 */

    open fun fixture(filename: String?): String? {
        return try {
            val url = Resources.getResource(filename)
            Resources.toString(url, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
    }
}


