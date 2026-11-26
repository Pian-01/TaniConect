import { Routes } from '@angular/router';
import { Beranda } from './pages/beranda/beranda'; // Sesuaikan nama kelas beranda Anda

export const routes: Routes = [
  { path: '', redirectTo: 'beranda', pathMatch: 'full' },
  { path: 'beranda', component: Beranda },
  // Nanti rute halaman lain bisa ditambahkan di sini
];