package com.example.hzh.library.http

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/07.
 */
object HttpsUtils {

    class SSLParams {

        var sslSocketFactory by Delegates.notNull<SSLSocketFactory>()

        var trustManager by Delegates.notNull<X509TrustManager>()
    }

    /**
     * 仅传 trustManager --> https单向认证
     * 可以额外配置信任服务端的证书策略，否则默认是按CA证书去验证的，若不是CA可信任的证书，则无法通过验证
     *
     * 仅传 certificates --> https单向认证
     * 用含有服务端公钥的证书校验服务端证书
     *
     * 传 bksFile、password、certificates --> https双向认证
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     * certificates -> 用含有服务端公钥的证书校验服务端证书
     *
     * 传 trustManager、bksFile、password --> https双向认证
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     * X509TrustManager -> 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
     */
    fun getSSLSocketFactory(
        trustManager: X509TrustManager? = null,
        bksFile: InputStream? = null,
        password: String? = null,
        vararg certificates: InputStream?
    ): SSLParams? {
        return SSLParams().apply {
            try {
                val keyManagers = prepareKeyManager(bksFile, password)
                val trustManagers = prepareTrustManager(*certificates)

                // 优先使用用户自定义的TrustManager
                // 然后使用默认的TrustManager
                // 否则使用不安全的TrustManager
                val manager = trustManager ?: getTrustManager(trustManagers) ?: unSafeTrustManager

                // 创建TLS类型的SSLContext对象
                val sslContext = SSLContext.getInstance("TLS")

                // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
                // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证
                // 第二个是被授权的证书管理器，用来验证服务器端的证书
                sslContext.init(keyManagers, arrayOf<TrustManager>(manager), null)

                // 通过sslContext获取SSLSocketFactory对象
                sslSocketFactory = sslContext.socketFactory
                this.trustManager = manager
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError(e)
            } catch (e: KeyManagementException) {
                throw AssertionError(e)
            }
        }
    }

    private val unSafeTrustManager: X509TrustManager = object : X509TrustManager {
        @Throws(CertificateException::class)

        @Suppress("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        @Suppress("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> =
            arrayOf()
    }

    private fun prepareKeyManager(bksFile: InputStream?, password: String?): Array<KeyManager>? {
        if (bksFile == null || password == null) return null

        try {
            KeyStore.getInstance("BKS")?.let {
                it.load(bksFile, password.toCharArray())

                return KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())?.run {
                    init(it, password.toCharArray())
                    keyManagers
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun prepareTrustManager(vararg certificates: InputStream?): Array<TrustManager>? {
        if (certificates.isEmpty()) return null

        try {
            // 创建一个默认类型的KeyStore，存储我们信任的证书
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)

            CertificateFactory.getInstance("X.509")?.run {
                var index = 0

                certificates.forEach {
                    val certAlias = (index++).toString()

                    // 证书工厂根据证书文件的流生成证书 cert
                    val cert = generateCertificate(it)
                    // 将 cert 作为可信证书放入到keyStore中
                    keyStore.setCertificateEntry(certAlias, cert)

                    try {
                        it?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            // 创建一个默认类型的TrustManagerFactory
            // 用之前的keyStore实例初始化TrustManagerFactory，这样就会信任keyStore中的证书
            // 获取TrustManager数组，TrustManager也会信任keyStore中的证书
            return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).run {
                init(keyStore)
                trustManagers
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager? {
        trustManagers?.forEach {
            if (it is X509TrustManager) return it
        }
        return null
    }
}