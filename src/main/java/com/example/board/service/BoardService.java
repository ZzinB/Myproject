package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    //DTO 객체를 엔티티 객체로 변환
    //BoardDTO의 List<String> fileNames를 Board의 Set<BoardImage>로 변환
    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if (boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    //Board엔티티 객체를 BoardDTO로 변환
    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() + "_" + boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }

}
