package grooine.data

/**
 * Object holding result data
 *
 * @since 0.1.0
 */
@Data
class Result<T> {
    Optional<T> first
    List<T> resultList
}
