#ifndef BROWSE_FILE_IO_HPP
#define BROWSE_FILE_IO_HPP

#include "browse/models.hpp"

#include <string>
#include <vector>

namespace browse {

std::vector<Category> read_categories(const std::string& path);
std::vector<Product> read_products(const std::string& path);
std::vector<Request> read_requests(const std::string& path);
void write_results(const std::string& path,
                   const std::vector<BrowseResult>& results);

}  // namespace browse

#endif  // BROWSE_FILE_IO_HPP
