package com.example.task_1.controller;

import com.example.task_1.dto.*;
import com.example.task_1.entity.*;
import com.example.task_1.service.ArticleService;
import com.example.task_1.util.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/article") // Article CRUD + reactions + comments
public class ArticleController {
  private final ArticleService service;
  public ArticleController(ArticleService s){ this.service = s; }

  @PostMapping
  @PreAuthorize("hasAuthority('USER')") // create requires USER
  public ResponseEntity<ArticleResponse> create(
      @Validated @RequestPart("payload") ArticleCreateRequest req, 
      @RequestPart(value="image", required=false) MultipartFile image, // optional image
      Authentication auth
  ) throws IOException {
    byte[] img = null;
    if (image != null) {
      if (image.getSize() > 500_000) throw new IllegalArgumentException("image-too-large");
      img = image.getBytes();
    }
    Article a = service.create(req, auth, img);
    return ResponseEntity.status(HttpStatus.CREATED).body(Mappers.toArticleResponse(a));
  }

  @GetMapping("/{id}") // public get by id (enabled only)
  public ResponseEntity<ArticleResponse> get(@PathVariable Long id){
    Article a = service.getEnabled(id);
    return ResponseEntity.ok(Mappers.toArticleResponse(a));
  }

  @GetMapping // public paged list of enabled articles
  public ResponseEntity<PageResponse<ArticleResponse>> list(Pageable pageable){
    Page<Article> page = service.list(pageable);
    List<ArticleResponse> content = page.getContent().stream().map(Mappers::toArticleResponse).toList();
    return ResponseEntity.ok(new PageResponse<>(
        page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), content));
  }

  @GetMapping("/{id}/image") // serve article image as JPEG
  public ResponseEntity<byte[]> image(@PathVariable Long id){
    Article a = service.getEnabled(id);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(a.getImage());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')") // service enforces ownership
  public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth){
    service.delete(id, auth);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/comment")
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<CommentResponse> comment(
      @PathVariable Long id,
      @Validated @RequestBody CommentCreateRequest req,
      Authentication auth
  ){
    Comment c = service.addComment(id, req, auth.getName());
    return ResponseEntity.ok(Mappers.toCommentResponse(c));
  }

  @GetMapping("/{id}/comment") // public comments list
  public ResponseEntity<List<CommentResponse>> comments(@PathVariable Long id){
    List<CommentResponse> list = service.listComments(id).stream().map(Mappers::toCommentResponse).toList();
    return ResponseEntity.ok(list);
  }

  @PutMapping("/{id}/like")
  @PreAuthorize("hasAuthority('USER')") // like requires USER
  public ResponseEntity<ArticleResponse> like(@PathVariable Long id){
    return ResponseEntity.ok(Mappers.toArticleResponse(service.like(id)));
  }

  @PutMapping("/{id}/dislike")
  @PreAuthorize("hasAuthority('USER')") // dislike requires USER
  public ResponseEntity<ArticleResponse> dislike(@PathVariable Long id){
    return ResponseEntity.ok(Mappers.toArticleResponse(service.dislike(id)));
  }

  @PutMapping("/{id}/disable")
  @PreAuthorize("hasAuthority('ADMIN')") // admin-only disable
  public ResponseEntity<ArticleResponse> disable(@PathVariable Long id){
    return ResponseEntity.ok(Mappers.toArticleResponse(service.disable(id, true)));
  }

  @PutMapping("/{id}/enable")
  @PreAuthorize("hasAuthority('ADMIN')") // admin-only enable
  public ResponseEntity<ArticleResponse> enable(@PathVariable Long id){
    return ResponseEntity.ok(Mappers.toArticleResponse(service.disable(id, false)));
  }
}
