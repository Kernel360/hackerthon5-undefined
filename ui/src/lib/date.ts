import { formatDistanceToNow, parseISO } from 'date-fns';
import { ko } from 'date-fns/locale';

/**
 * Converts a Spring LocalDateTime string to a JavaScript Date
 * Spring format example: "2024-02-22T20:34:31"
 */
export const parseSpringDateTime = (dateString: string): Date => {
  // If the date string doesn't have a timezone, assume it's in local time
  if (!dateString.includes('Z') && !dateString.includes('+')) {
    return parseISO(dateString);
  }
  return new Date(dateString);
};

/**
 * Formats a Spring LocalDateTime string to a relative time string (e.g., "3시간 전")
 */
export const formatRelativeTime = (dateString: string): string => {
  const date = parseSpringDateTime(dateString);
  return formatDistanceToNow(date, { addSuffix: true, locale: ko });
};
