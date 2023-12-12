package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardCategory", "boardId"));
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // getPageNumber()는 현재 보고 있는 페이지
        int pageLimit = 15; // 한 페이지에 "게시글" 몇 개를 보여줄 것인가 (지금은 15개)

        Page<BoardEntity> boardEntityPage = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardCategory", "boardId")));

        Page<BoardDTO> boardDTOPage = boardEntityPage.map(
                postPage -> BoardDTO.toBoardDTO(postPage)
        );

        return boardDTOPage;
    }

    public BoardDTO findByBoardId(Long boardId) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);

        // DB에 값이 존재하면 Entity를 DTO로 변환
        if (optionalBoardEntity.isPresent())
            return BoardDTO.toBoardDTO(optionalBoardEntity.get());
        else
            return null;
    }

    public void updateViewCnt(Long boardId) {
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);

        if (boardEntity.isPresent()) {
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity.get());
            boardDTO.setBoardViewCnt(boardDTO.getBoardViewCnt() + 1);
            boardRepository.save(BoardEntity.toBoardEntity(boardDTO));
        }
    }

    public void write(BoardDTO boardDTO, String mbId, MultipartFile file) throws Exception {
        String savePath = "D:\\spring_board_savefile";

        // 고유 식별자, 임의로 파일이름을 바꿔주기 위한 값
        UUID uuid = UUID.randomUUID();

        // 파일 이름 짓기
        String fileName = uuid + "_" + file.getOriginalFilename();

        // 어떤 경로에 어떤 파일을 저장할 것인가
        File saveFile = new File(savePath, fileName);

        // saveFile에 명시한 내용을 가지고 파일 업로드 처리(transferTo)
        file.transferTo(saveFile);

        // 게시글 작성시 최초 값 초기화
        boardDTO.setBoardMemberWriter(memberRepository.findByMemberId(mbId).orElse(null));
        boardDTO.setBoardCommentCnt(0l);
        boardDTO.setBoardDislike(0l);
        boardDTO.setBoardLike(0l);
        boardDTO.setBoardViewCnt(0l);
        boardDTO.setBoardDislike(0l);
        boardDTO.setFileName(fileName);
        boardDTO.setFilePath("/" + file.getOriginalFilename());

        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public void deleteByBoardDTO(BoardDTO boardDTO) {
        boardRepository.deleteById(boardDTO.getBoardId());
    }

    public void update(BoardDTO exDTO, BoardDTO boardDTO) {
        exDTO.setBoardTitle(boardDTO.getBoardTitle()); // 기존 게시글의 제목을 새로운 게시글의 제목으로 덮어 씌운다.
        exDTO.setBoardContent(boardDTO.getBoardContent()); // 기존 게시글의 내용을 새로운 게시글의 내용으로 덮어 씌운다.

        // DTO -> Entity로 바꾼다.
        BoardEntity boardEntity = BoardEntity.toBoardEntity(exDTO);
        boardRepository.save(boardEntity);
    }

    public void like(BoardDTO boardDTO) {
        boardDTO.setBoardLike(boardDTO.getBoardLike() + 1);

        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public void dislike(BoardDTO boardDTO) {
        boardDTO.setBoardDislike(boardDTO.getBoardDislike() + 1);

        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public List<BoardDTO> findByBoardWriter(String memberNic) {
        List<BoardEntity> boardEntityList = boardRepository.findAllByBoardMemberWriter_MemberNic(memberNic);
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }

    public int findByBoardWriteCnt(String memberNic) {
        List<BoardEntity> boardEntityList = boardRepository.findAllByBoardMemberWriter_MemberNic(memberNic); // memberNic으로 게시글을 찾음

        return boardEntityList.size();
    }

}