package com.spacer.sdk.data

interface IMapper<TSource, TDestination> {
    fun map(source: TSource): TDestination
}
