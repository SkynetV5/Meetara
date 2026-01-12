import { useEffect } from "react";

export default function Title({ title, children }) {
  useEffect(() => {
    document.title = title;
  }, [title]);

  return children;
}
