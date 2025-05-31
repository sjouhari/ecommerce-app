export interface CommentModel {
    id?: number;
    content: string;
    productId: number;
    userId: number;
    username?: string;
    rating?: number;
    createdAt?: Date;
}
