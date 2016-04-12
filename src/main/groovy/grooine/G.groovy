package grooine

import java.util.concurrent.CompletableFuture
import groovy.transform.CompileStatic

import grooine.data.Result

/**
 * Public API
 *
 * @since 0.1.0
 */
final class G {

    /**
     * Retrieves a list of the available topics
     *
     * @param params
     * @return a {@link CompletableFuture} {@link Result} containing {@link
     * Topic} instances
     * @since 0.1.0
     */
    static CompletableFuture<Result<Map>> topics(final Map params) {
        return CompletableFuture.supplyAsync { -> F.topics(params) }
    }

    /**
     * @param code
     * @param params
     * @return
     * @since 0.1.0
     */
    static CompletableFuture<Result<Map>> series(final String code, final Map params) {
        return CompletableFuture.supplyAsync { -> F.series(code, params) }
    }
}
