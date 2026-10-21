import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

interface Album {
  id: number;
  name: string;
  accessCode: string;
  createdAt: string;
}

interface Photo {
  id: number;
  originalName: string;
  contentType: string;
  fileSize: number;
  uploadedAt: string;
}

@Component({
  selector: 'jhi-album-gallery',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './album-gallery.component.html',
  styleUrl: './album-gallery.component.scss',
})
export default class AlbumGalleryComponent implements OnInit {
  private readonly http = inject(HttpClient);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);

  accessCode = '';
  album?: Album;
  photos: Photo[] = [];

  loading = true;
  errorMessage = '';

  selectedPhoto?: Photo;

  ngOnInit(): void {
    this.accessCode = this.route.snapshot.paramMap.get('accessCode') ?? '';

    if (!this.accessCode) {
      void this.router.navigate(['/']);
      return;
    }

    this.loadAlbum();
  }

  openPhoto(photo: Photo): void {
    this.selectedPhoto = photo;
  }

  closePhoto(): void {
    this.selectedPhoto = undefined;
  }

  thumbnailUrl(photo: Photo): string {
    return `/api/public/albums/${this.accessCode}/photos/${photo.id}/thumbnail`;
  }

  previewUrl(photo: Photo): string {
    return `/api/public/albums/${this.accessCode}/photos/${photo.id}/preview`;
  }

  private loadAlbum(): void {
    this.http.get<Album>(`/api/public/albums/${this.accessCode}`).subscribe({
      next: album => {
        this.album = album;
        this.loadPhotos();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No pudimos encontrar esta galería.';

        this.cdr.detectChanges();
      },
    });
  }

  private loadPhotos(): void {
    this.http.get<Photo[]>(`/api/public/albums/${this.accessCode}/photos`).subscribe({
      next: photos => {
        this.photos = photos;
        this.loading = false;

        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No pudimos cargar las fotografías.';

        this.cdr.detectChanges();
      },
    });
  }
}
