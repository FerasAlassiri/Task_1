import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-pagination',
  imports: [CommonModule],
  template: `
  <div *ngIf="totalPages > 1" class="row">
    <button (click)="prev()" [disabled]="page<=0">Prev</button>
    <span>Page {{ page+1 }} / {{ totalPages }}</span>
    <button (click)="next()" [disabled]="page+1>=totalPages">Next</button>
  </div>
  `,
  styles: [`.row{display:flex;gap:12px;align-items:center;margin:12px 0}`]
})
export class PaginationComponent {
  @Input() page = 0;
  @Input() totalPages = 1;
  @Output() pageChange = new EventEmitter<number>();
  prev(){ if (this.page>0) this.pageChange.emit(this.page-1); }
  next(){ if (this.page+1<this.totalPages) this.pageChange.emit(this.page+1); }
}