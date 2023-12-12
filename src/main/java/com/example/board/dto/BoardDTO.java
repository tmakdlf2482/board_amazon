package com.example.board.dto;

import com.example.board.entity.BoardEntity;
import com.example.board.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long boardId;
    private boolean boardCategory;
    private MemberEntity boardMemberWriter;
    private Date boardDate;
    private String boardTitle;
    private String boardContent;
    private Long boardViewCnt;
    private Long boardLike;
    private Long boardDislike;
    private Long boardCommentCnt;
    private String fileName;
    private String filePath;

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardId(boardEntity.getBoardId());
        boardDTO.setBoardCategory(boardEntity.isBoardCategory());
        boardDTO.setBoardMemberWriter(boardEntity.getBoardMemberWriter());
        boardDTO.setBoardDate(boardEntity.getBoardDate());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContent(boardEntity.getBoardContent());
        boardDTO.setBoardViewCnt(boardEntity.getBoardViewCnt());
        boardDTO.setBoardLike(boardEntity.getBoardLike());
        boardDTO.setBoardDislike(boardEntity.getBoardDislike());
        boardDTO.setBoardCommentCnt(boardEntity.getBoardCommentCnt());
        boardDTO.setFileName(boardEntity.getFileName());
        boardDTO.setFilePath(boardEntity.getFilePath());

        return boardDTO;
    }

    BoardDTO(BoardEntity boardEntity) {
        this.boardId = boardEntity.getBoardId();
        this.boardCategory = boardEntity.isBoardCategory();
        this.boardMemberWriter = boardEntity.getBoardMemberWriter();
        this.boardDate = boardEntity.getBoardDate();
        this.boardTitle = boardEntity.getBoardTitle();
        this.boardContent = boardEntity.getBoardContent();
        this.boardViewCnt = boardEntity.getBoardViewCnt();
        this.boardLike = boardEntity.getBoardLike();
        this.boardDislike = boardEntity.getBoardDislike();
        this.boardCommentCnt = boardEntity.getBoardCommentCnt();
    }

}