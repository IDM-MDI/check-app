package ru.clevertec.test.checkapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.test.checkapp.entity.DiscountCard;
import ru.clevertec.test.checkapp.exception.ServiceException;
import ru.clevertec.test.checkapp.model.DiscountCardModel;
import ru.clevertec.test.checkapp.repository.DiscountCardRepository;
import ru.clevertec.test.checkapp.service.DiscountCardService;
import ru.clevertec.test.checkapp.util.impl.DiscountCardModelMapper;

import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_ALREADY_EXIST;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_FOUND;
import static ru.clevertec.test.checkapp.exception.ExceptionCode.ENTITY_NOT_VALID;
import static ru.clevertec.test.checkapp.validator.DiscountCardValidator.isDiscountCardValid;

public class DiscountCardServiceImpl implements DiscountCardService {
    private final DiscountCardRepository repository;
    private final DiscountCardModelMapper modelMapper;

    @Autowired
    public DiscountCardServiceImpl(DiscountCardRepository repository, DiscountCardModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DiscountCardModel findByID(long id) throws ServiceException {
        return modelMapper.toModel(repository.findById(id)
                .orElseThrow(() -> new ServiceException(ENTITY_NOT_FOUND.toString())));
    }
    @Override
    public DiscountCardModel save(DiscountCardModel model) throws ServiceException {
        beforeSaveValidating(model);
        DiscountCard savedCard = repository.save(modelMapper.toEntity(model));
        return modelMapper.toModel(savedCard);
    }

    @Override
    public void delete(long id) throws ServiceException {
        if(!repository.existsById(id)) {
            throw new ServiceException(ENTITY_NOT_FOUND.toString());
        }
        repository.deleteById(id);
    }

    private void beforeSaveValidating(DiscountCardModel model) throws ServiceException {
        model.setId(0);
        if(!isDiscountCardValid(model)) {
            throw new ServiceException(ENTITY_NOT_VALID.toString());
        } else if(repository.existsDiscountCardByNumber(model.getNumber())) {
            throw new ServiceException(ENTITY_ALREADY_EXIST.toString());
        }
    }
}
