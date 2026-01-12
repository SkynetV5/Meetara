export default {
    api: {
      output: {
        mode: "split",
        target: "./src/api/endpoints",
        client: "react-query",
        override: {
          mutator: {
            path: "./src/api/axios.js",
            name: "axios",
          },
          query: {
            useInfinite: false,
            useQuery: true,
          },
        },
      },
      input: "./swagger.json",
    },
  };