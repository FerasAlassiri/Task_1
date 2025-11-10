package com.example.task_1.service;

import com.example.task_1.dto.*;
import com.example.task_1.entity.*;
import com.example.task_1.exception.ForbiddenException;
import com.example.task_1.exception.NotFoundException;
import com.example.task_1.repository.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articles;
    private final CommentRepository comments;

    public ArticleService(ArticleRepository a, CommentRepository c) {
        this.articles = a;
        this.comments = c;
    }

    @Transactional // create new article
    public Article create(ArticleCreateRequest req, Authentication auth, byte[] image) {
        Article a = new Article();
        a.setTitle(req.title());
        a.setBody(req.body());
        a.setAuthor(auth.getName());
        if (image != null) a.setImage(image);
        return articles.save(a);
    }

    // list all enabled (non-disabled) articles with pagination
    public Page<Article> list(Pageable pageable) {
        return articles.findEnabled(pageable);
    }

    // get one enabled article or throw exception
    public Article getEnabled(Long id) {
        Article a = articles.findById(id).orElseThrow(() -> new NotFoundException("article-not-found"));
        if (a.isDisabled()) throw new NotFoundException("article-disabled");
        return a;
    }

    @Transactional // delete only if requester is the author
    public void delete(Long id, Authentication auth) {
        Article a = articles.findById(id).orElseThrow(() -> new NotFoundException("article-not-found"));
        if (!a.getAuthor().equals(auth.getName())) throw new ForbiddenException("not-owner");
        articles.delete(a);
    }

    @Transactional // increment like count
    public Article like(Long id) {
        Article a = articles.findById(id).orElseThrow(() -> new NotFoundException("article-not-found"));
        a.setLikesCount(a.getLikesCount() + 1);
        return a;
    }

    @Transactional // increment dislike count
    public Article dislike(Long id) {
        Article a = articles.findById(id).orElseThrow(() -> new NotFoundException("article-not-found"));
        a.setDislikesCount(a.getDislikesCount() + 1);
        return a;
    }

    @Transactional // enable or disable article (admin only)
    public Article disable(Long id, boolean disable) {
        Article a = articles.findById(id).orElseThrow(() -> new NotFoundException("article-not-found"));
        a.setDisabled(disable);
        return a;
    }

    // --- Comments section ---

    @Transactional // add a new comment to an article
    public Comment addComment(Long articleId, CommentCreateRequest req, String username) {
        Article a = getEnabled(articleId);
        Comment c = new Comment();
        c.setArticle(a);
        c.setText(req.text());
        c.setUsername(username);
        return comments.save(c);
    }

    // list all comments of a given article
    public List<Comment> listComments(Long articleId) {
        getEnabled(articleId);
        return comments.findByArticleId(articleId);
    }
}
