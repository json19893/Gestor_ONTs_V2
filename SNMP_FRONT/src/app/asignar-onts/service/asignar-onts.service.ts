import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { olts } from '../interfaces';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OltsService {
  private URL:string = "http://localhost:3000/OntSinAsignar";

  constructor(private http: HttpClient) {}

  getOlts(): Observable<olts[]>{
    return this.http.get<olts[]>(this.URL);
  }
}
