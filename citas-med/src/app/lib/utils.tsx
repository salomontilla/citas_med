import { CalendarDate } from "@internationalized/date";

export function formatearFecha(fecha: CalendarDate | null): string | null {
  if (!fecha) return null;

  const year = fecha.year;
  const month = String(fecha.month).padStart(2, '0');
  const day = String(fecha.day).padStart(2, '0');

  return `${year}-${month}-${day}`;
}
export function formatearHora(hora: string | null): string | null {
  if (!hora) return null;
  return hora?.substring(hora?.indexOf('-') + 1) + ":00";
}