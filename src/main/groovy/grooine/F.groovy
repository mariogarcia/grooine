package grooine

import okhttp3.Request
import okhttp3.Response
import okhttp3.OkHttpClient

import groovy.json.JsonSlurper
import groovy.json.internal.LazyMap
import groovy.util.logging.Log
import groovy.transform.CompileStatic

import grooine.data.Result

/**
 * @since 0.1.0
 */
@Log
@CompileStatic
class F {

    /**
     * Lists all available topics at INE
     *
     * @param params query params
     * @return a {@link Result} containing all available topics
     * @since 0.1.0
     */
    static Result<Map> topics(final Map params) {
        return executeFn('OPERACIONES_DISPONIBLES', '', params)
    }

    /**
     * @param code
     * @param params query params
     * @return
     * @since 0.1.0
     */
    static Result<Map> series(final String code, final Map params) {
        return executeFn("DATOS_SERIE", code, params)
    }

    /**
     * @param code
     * @param params query params
     * @return
     * @since 0.1.0
     */
    static Result<Map> seriesTableOp(final String code, final Map params) {
        return executeFn("SERIES_TABLAOPERACION", code, params)
    }

    /**
     * Builds the URI and executes it
     *
     * @param fn the function to execute
     * @param code
     * @param param
     * @return
     * @since 0.1.0
     */
    static Result<Map> executeFn(final String fn, final String code, final Map params) {
        String uri = buildURI(fn, code, params)

        return process(uri, Map)
    }

    /**
     * Builds an URI out of the function + input + query parameters
     *
     * @param function
     * @param input
     * @param params
     * @return a URI string
     * @since 0.1.0
     */
    static String buildURI(final String function, final String input, final Map params) {
        String language   = params?.language ?: 'ES'
        String parameters = params?.collect({ "${it.key}=${it.value}" })?.join("&") ?: ''
        String resultURI  = "http://servicios.ine.es/wstempus/js/$language/$function/$input?$parameters"

        log.info(resultURI)

        return resultURI
    }

    /**
     * @param uri
     * @param clazz
     * @return an instance of {@link Result}
     * @since 0.1.0
     */
    static <T> Result<T> process(final String uri, T clazz) {
        Request request      = new Request.Builder().url(uri).build()
        OkHttpClient client  = new OkHttpClient()
        Response response    = client.newCall(request).execute()
        String jsonString    = response.body().string()
        List<LazyMap> result = (List<LazyMap>)new JsonSlurper().parseText(jsonString)

        List<T> resultList = result.grep().collect { (T) it }
        Optional<T> first  = Optional.ofNullable(resultList.find())

        return new Result<T>(resultList: resultList, first: first)
    }
}
