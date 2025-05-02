import { Component, output, signal } from '@angular/core';
import { PrimeNG } from 'primeng/config';
import { FileUpload } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { BadgeModule } from 'primeng/badge';
import { HttpClientModule } from '@angular/common/http';

@Component({
    selector: 'app-image-upload',
    templateUrl: './image-upload.component.html',
    standalone: true,
    imports: [FileUpload, ButtonModule, BadgeModule, CommonModule, HttpClientModule]
})
export class ImageUploadComponent {
    files = output<File[]>();
    multiple = signal(false);

    constructor(private config: PrimeNG) {}

    choose(event: any, callback: any) {
        callback();
    }

    onRemoveTemplatingFile(event: any, file: any, removeFileCallback: any, index: any) {
        removeFileCallback(event, index);
    }

    onClearTemplatingUpload(clear: any) {
        clear();
    }

    onSelectedFiles(event: any) {
        this.files.emit(event.files);
    }

    uploadEvent(callback: any) {
        callback();
    }

    formatSize(bytes: any) {
        const k = 1024;
        const dm = 3;
        const sizes = this.config.translation.fileSizeTypes;
        if (bytes === 0) {
            return `0 ${sizes ? sizes[0] : ''}`;
        }

        const i = Math.floor(Math.log(bytes) / Math.log(k));
        const formattedSize = parseFloat((bytes / Math.pow(k, i)).toFixed(dm));

        return `${formattedSize} ${sizes ? sizes[i] : ''}`;
    }
}
