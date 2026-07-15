#ifndef BROWSE_ENGINE_HPP
#define BROWSE_ENGINE_HPP

#include "browse/models.hpp"

#include <string>
#include <unordered_map>
#include <vector>

namespace browse {

class Engine {
public:
    static Engine from_categories(const std::vector<Category>& categories);

    std::vector<BrowseResult> evaluate(
        const std::vector<Request>& requests,
        const std::vector<Product>& products) const;

private:
    explicit Engine(
        std::unordered_map<std::string, std::vector<std::string>> children);

    std::vector<std::string> collect_included_categories(
        const std::string& root_category_id) const;

    std::unordered_map<std::string, std::vector<std::string>> children_;
};

}  // namespace browse

#endif  // BROWSE_ENGINE_HPP
