package io.graversen.trunk.mapper;

import org.modelmapper.ModelMapper;

import java.util.Optional;

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
}
