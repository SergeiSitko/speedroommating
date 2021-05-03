package by.sitko.speedroommating

abstract class Mapper<T1, T2> {

    abstract fun map(value: T1): T2

    fun map(values: List<T1>): List<T2> {
        return values.map { map(it) }
    }
}