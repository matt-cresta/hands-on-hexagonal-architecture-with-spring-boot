package ai.cresta.mappers;

import ai.cresta.entity.Book;
import ai.cresta.data.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto bookToBookDto(Book book);

    Book bookDtoToBook(BookDto bookDto);

    List<BookDto> bookListToBookDtoList(List<Book> bookList);

    List<Book> BookDtoListTobookList(List<BookDto> BookDtoList);
}
