# Key Fixes

- Map `product_id`, `name`, and `popularity_score` to the correct product fields.
- Build a parent-to-children adjacency map from `Category::parent_id`.
- Return no categories for an unknown requested category.
- Keep only products in the requested category subtree.
- Apply an exact, case-sensitive region filter.
- Calculate `matched_count` before truncation.
- Sort popularity descending and use product ID ascending as the tie-breaker.
