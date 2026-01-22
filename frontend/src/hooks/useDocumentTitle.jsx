import { useEffect } from 'react';

/**
 * Hook to update dynamically the document title
 * @param {string} title - Title of the page
 * @param {string} suffix - Suffix optional (default: "ChurnCheck")
 */
export const useDocumentTitle = (title, suffix = "ChurnCheck") => {
  useEffect(() => {
    if (title) {
      document.title = `${title} | ${suffix}`;
    } else {
      document.title = suffix;
    }
  }, [title, suffix]);

  // Cleanup for restore the original title
  useEffect(() => {
    return () => {
      document.title = "ChurnCheck - Gym Analytics Platform";
    };
  }, []);
};
