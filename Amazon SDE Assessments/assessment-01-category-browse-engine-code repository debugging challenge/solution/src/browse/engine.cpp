#include "browse/engine.hpp"

#include <algorithm>
#include <string>
#include <unordered_set>
#include <utility>
#include <vector>

namespace browse {

Engine::Engine(
    std::unordered_map<std::string, std::vector<std::string>> children)
    : children_(std::move(children)) {}

Engine Engine::from_categories(const std::vector<Category>& categories) {
    std::unordered_map<std::string, std::vector<std::string>> children;

    for (const Category& category : categories) {
        children[category.category_id];
        if (category.parent_id.has_value()) {
            children[*category.parent_id].push_back(category.category_id);
        }
    }

    return Engine(std::move(children));
}

std::vector<std::string> Engine::collect_included_categories(
    const std::string& root_category_id) const {
    if (children_.find(root_category_id) == children_.end()) {
        return {};
    }

    std::vector<std::string> included;
    std::vector<std::string> stack{root_category_id};
    std::unordered_set<std::string> visited;

    while (!stack.empty()) {
        const std::string current = stack.back();
        stack.pop_back();

        if (!visited.insert(current).second) {
            continue;
        }
        included.push_back(current);

        const auto it = children_.find(current);
        if (it == children_.end()) {
            continue;
        }
        for (const std::string& child : it->second) {
            stack.push_back(child);
        }
    }

    return included;
}

std::vector<BrowseResult> Engine::evaluate(
    const std::vector<Request>& requests,
    const std::vector<Product>& products) const {
    std::vector<BrowseResult> results;
    results.reserve(requests.size());

    for (const Request& request : requests) {
        const std::vector<std::string> included_vector =
            collect_included_categories(request.category_id);
        const std::unordered_set<std::string> included(
            included_vector.begin(), included_vector.end());

        std::vector<Product> eligible;
        for (const Product& product : products) {
            if (included.find(product.category_id) == included.end() ||
                product.region != request.region) {
                continue;
            }
            eligible.push_back(product);
        }

        std::sort(eligible.begin(), eligible.end(),
                  [](const Product& left, const Product& right) {
            if (left.popularity_score != right.popularity_score) {
                return left.popularity_score > right.popularity_score;
            }
            return left.product_id < right.product_id;
        });

        BrowseResult result;
        result.query_id = request.query_id;
        result.matched_count = static_cast<int>(eligible.size());

        int limit = request.max_results;
        if (limit < 0) {
            limit = 0;
        }
        limit = std::min(limit, static_cast<int>(eligible.size()));

        result.products.reserve(static_cast<std::size_t>(limit));
        for (int i = 0; i < limit; ++i) {
            result.products.push_back(ResultProduct{
                eligible[i].product_id,
                eligible[i].name,
                eligible[i].category_id,
                eligible[i].popularity_score
            });
        }

        results.push_back(std::move(result));
    }

    return results;
}

}  // namespace browse
