#ifndef BROWSE_MODELS_HPP
#define BROWSE_MODELS_HPP

#include <optional>
#include <string>
#include <vector>

namespace browse {

struct Category {
    std::string category_id;
    std::string name;
    std::optional<std::string> parent_id;
};

struct Product {
    std::string product_id;
    std::string name;
    std::string category_id;
    std::string region;
    int popularity_score = 0;
};

struct Request {
    std::string query_id;
    std::string category_id;
    std::string region;
    int max_results = 5;
};

struct ResultProduct {
    std::string product_id;
    std::string name;
    std::string category_id;
    int popularity_score = 0;
};

struct BrowseResult {
    std::string query_id;
    int matched_count = 0;
    std::vector<ResultProduct> products;
};

}  // namespace browse

#endif  // BROWSE_MODELS_HPP
