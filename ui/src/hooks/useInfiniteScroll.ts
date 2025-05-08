import { useEffect, useRef, useState } from 'react';
import { BlogPost } from '@/types/blog';

export function useInfiniteScroll(initialData: BlogPost[]) {
  const [posts, setPosts] = useState<BlogPost[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const loaderRef = useRef(null);
  const POSTS_PER_PAGE = 8;

  // 실제 프로젝트에서는 API 호출로 대체
  const fetchMorePosts = () => {
    const startIndex = (page - 1) * POSTS_PER_PAGE;
    const endIndex = startIndex + POSTS_PER_PAGE;
    const newPosts = initialData.slice(startIndex, endIndex);
    
    if (newPosts.length === 0) {
      setHasMore(false);
      return;
    }

    setPosts(prev => [...prev, ...newPosts]);
    setPage(prev => prev + 1);
  };

  useEffect(() => {
    fetchMorePosts();
  }, []);

  useEffect(() => {
    const observer = new IntersectionObserver(
      entries => {
        if (entries[0].isIntersecting && hasMore) {
          fetchMorePosts();
        }
      },
      { threshold: 1.0 }
    );

    if (loaderRef.current) {
      observer.observe(loaderRef.current);
    }

    return () => observer.disconnect();
  }, [hasMore, page]);

  return { posts, loaderRef, hasMore };
} 