package io.graversen.trunk.mapper;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Martin
 */
public class ObjectMapper extends ModelMapper
{
    public <D> Optional<D> mapNullable(Object source, Class<D> destinationType)
    {
        if (source == null)
        {
            return Optional.empty();
        }
        else
        {
            return Optional.of(super.map(source, destinationType));
        }
    }

    public <T, R> Function<T, R> map(Class<R> destinationType)
    {
        return object -> super.map(object, destinationType);
    }

    public <T> Collection<T> mapCollection(Collection<?> collection, Class<T> destinationType)
    {
        if (collection.isEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            return collection.stream().map(object -> super.map(object, destinationType)).collect(Collectors.toList());
        }
    }

    public <T> Collection<T> mapIterable(Iterable<?> iterable, Class<T> destinationType)
    {
        final Collection<?> collection = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        return this.mapCollection(collection, destinationType);
    }
}
