import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentModel } from '../models/product/comment.model';

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    http = inject(HttpClient);

    baseUrl = 'http://localhost:8080/api/comments';

    getComments(): Observable<CommentModel[]> {
        return this.http.get<CommentModel[]>(this.baseUrl);
    }

    getProductComments(productId: number): Observable<CommentModel[]> {
        return this.http.get<CommentModel[]>(`${this.baseUrl}/product/${productId}`);
    }

    getUserComments(userId: number): Observable<CommentModel[]> {
        return this.http.get<CommentModel[]>(`${this.baseUrl}/user/${userId}`);
    }

    createComment(comment: CommentModel): Observable<CommentModel> {
        return this.http.post<CommentModel>(this.baseUrl, comment);
    }

    updateComment(comment: CommentModel): Observable<CommentModel> {
        return this.http.put<CommentModel>(`${this.baseUrl}/${comment.id}`, comment);
    }

    deleteComment(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
