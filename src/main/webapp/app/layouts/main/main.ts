import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';

import { NavigationEnd, Router, RouterOutlet } from '@angular/router';

import { toSignal } from '@angular/core/rxjs-interop';

import { filter, map, startWith } from 'rxjs/operators';

import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import { AccountService } from 'app/core/auth/account.service';

import Footer from '../footer/footer';
import PageRibbon from '../profiles/page-ribbon';

@Component({
  selector: 'jhi-main',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './main.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, Footer, PageRibbon],
})
export default class Main implements OnInit {
  private readonly router = inject(Router);

  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);

  private readonly accountService = inject(AccountService);

  readonly isPublicView = toSignal(
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => this.isPublicUrl(this.router.url)),
      startWith(this.isPublicUrl(this.router.url)),
    ),
    {
      initialValue: this.isPublicUrl(this.router.url),
    },
  );

  ngOnInit(): void {
    // Intenta recuperar automáticamente la sesión existente.
    this.accountService.identity().subscribe();
  }

  private isPublicUrl(url: string): boolean {
    const path = url.split('?')[0].split('#')[0];

    return path === '/' || path.startsWith('/album/');
  }
}
