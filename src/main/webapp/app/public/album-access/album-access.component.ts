import { Component, HostListener, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface AlbumResponse {
  id: number;
  name: string;
  accessCode: string;
  createdAt: string;
}

@Component({
  selector: 'jhi-album-access',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './album-access.component.html',
  styleUrl: './album-access.component.scss',
})
export default class AlbumAccessComponent {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  accessCode = '';
  loading = false;
  errorMessage = '';

  parallaxX = 0;
  parallaxY = 0;

  @HostListener('window:pointermove', ['$event'])
  onPointerMove(event: PointerEvent): void {
    const x = event.clientX / window.innerWidth - 0.5;
    const y = event.clientY / window.innerHeight - 0.5;

    this.parallaxX = x;
    this.parallaxY = y;
  }

  openAlbum(): void {
    const code = this.accessCode.trim().toUpperCase();

    if (!code) {
      this.errorMessage = 'Ingresa el código de tu álbum.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.http.get<AlbumResponse>(`/api/public/albums/${code}`).subscribe({
      next: () => {
        this.loading = false;

        void this.router.navigate(['/album', code]);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'No encontramos un álbum con ese código.';
      },
    });
  }
}
